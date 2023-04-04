package com.control.empleados.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.control.empleados.model.Empleado;

public interface IEmpleadoRepository extends PagingAndSortingRepository<Empleado, Long> {

}
