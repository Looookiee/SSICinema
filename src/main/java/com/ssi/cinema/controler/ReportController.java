package com.ssi.cinema.controler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.ssi.cinema.backend.data.entity.Reservation;
import com.ssi.cinema.backend.service.ReservationService;
import com.ssi.cinema.model.Report;
import com.ssi.cinema.model.ReportObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public class ReportController {

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(value = "/generateReport", method = RequestMethod.POST)
    public ModelAndView handleUserRegister(HttpServletRequest request, @ModelAttribute("report") Report report) {

        ModelAndView model = new ModelAndView();
        try {
            Map<ReportObject, Long> reservationsMap = StreamSupport.stream(reservationService.findAll().spliterator(), false)
                    .filter(reservation -> addToReport(reservation, report))
                    .collect(Collectors.groupingBy(r -> buildReportObject(r), Collectors.counting()));
            List<ReportObject> reservations = reservationsMap.keySet().stream()
                    .map(reservation -> {
                        reservation.setTickets(reservationsMap.get(reservation));
                        return reservation; })
                    .collect(Collectors.toList());
            model.addObject("reservations", reservations);

        }
        catch (Exception e) {
            model.addObject("message", "Problems with generating report!");
        }

        model.setViewName("report");
        return model;
    }

    boolean addToReport (Reservation reservation, Report report){
        return (report.getCinema().isEmpty() || (reservation.getCinema().getName().equals(report.getCinema())))
                && (report.getMovie().isEmpty() || (reservation.getMovie().getName().equals(report.getMovie())))
                && (reservation.getDate().getYear() + 100 == Integer.parseInt(report.getYear()))
                && (reservation.getDate().getMonth() == Integer.parseInt(report.getMonth()))
                && (report.getDay().isEmpty() || (reservation.getDate().getDate() == Integer.parseInt(report.getDay())));
    }

    ReportObject buildReportObject(Reservation reservation){
        return new ReportObject(reservation.getCinema().getName(),
                reservation.getMovie().getName(), reservation.getDate());
    }
}