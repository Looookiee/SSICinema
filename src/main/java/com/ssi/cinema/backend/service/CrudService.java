package com.ssi.cinema.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.ssi.cinema.backend.data.entity.AbstractEntity;

public abstract class CrudService<T extends AbstractEntity> {
	
	protected abstract CrudRepository<T, Long> getRepository();
	
	public T save(T entity) {
		return getRepository().save(entity);
	}
	
	public void delete(long id) {
		getRepository().delete(id);
	}
	
	public T load(long id) {
		return getRepository().findOne(id);
	}
	
	public abstract long countAnyMatching(Optional<String> filter);
	
	public abstract Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);
	
	public Iterable<T> findAll() {
		return getRepository().findAll();
	}
	
}
