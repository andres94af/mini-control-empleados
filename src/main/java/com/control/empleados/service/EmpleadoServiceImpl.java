package com.control.empleados.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.control.empleados.model.Empleado;
import com.control.empleados.repository.IEmpleadoRepository;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService{
	
	@Autowired
	private IEmpleadoRepository repo;

	@Override
	@Transactional(readOnly = true)
	public List<Empleado> findAll() {
		return (List<Empleado>) repo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Empleado> findAll(Pageable pageable) {
		return repo.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Empleado empleado) {
		repo.save(empleado);
	}

	@Override
	@Transactional(readOnly = true)
	public Empleado findOne(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		repo.deleteById(id);;
	}

}
