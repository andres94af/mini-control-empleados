package com.control.empleados.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.control.empleados.model.Empleado;
import com.control.empleados.service.IEmpleadoService;
import com.control.empleados.util.paginacion.PageRender;
import com.control.empleados.util.reportes.EmpleadoExporterPDF;
import com.lowagie.text.DocumentException;

@Controller
public class EmpleadoController {

	@Autowired
	private IEmpleadoService empleadoService;

	
	@GetMapping("/ver/{id}")
	public String verDetalleDeEmpleado(@PathVariable Long id, Map<String, Object> modelo, RedirectAttributes flash) {
		Empleado empleado = empleadoService.findOne(id);
		if (empleado == null) {
			flash.addFlashAttribute("error", "El id del empleado no existe en la BBDD");
			return "redirect:/listar";
		}
		modelo.put("empleado", empleado);
		modelo.put("titulo", "Detalles del empleado "+empleado.getNombre());
		return "detalles_empleado";
	}
	
	@GetMapping({"/", "/listar", ""})
	public String listarEmpleados(@RequestParam(name="page", defaultValue = "0") int page, Model modelo) {
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Empleado> empleados = empleadoService.findAll(pageRequest);
		PageRender<Empleado> pageRender = new PageRender<>("/listar", empleados);
		modelo.addAttribute("titulo", "Listado de empleados");
		modelo.addAttribute("empleados", empleados);
		modelo.addAttribute("page", pageRender);
		return "listar";
	}
	
	@GetMapping("/form")
	public String mostrarFormularioDeRegistro(Map<String, Object> modelo) {
		Empleado empleado = new Empleado();
		modelo.put("empleado", empleado);
		modelo.put("titulo", "Registro de empleado");
		return "formulario";
	}
	
	@PostMapping("/form")
	public String guardarEmpleado(@Valid Empleado empleado, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			modelo.addAttribute("titulo", "Registro de empleado");
			return "formulario";
		}
		String mensaje = (empleado.getId() != null) ? "El empleado ha sido editado con exito" : "Empleado registrado con exito";
		
		empleadoService.save(empleado);
		status.setComplete();
		flash.addFlashAttribute("success", mensaje);
		return "redirect:/listar";
	}
	
	@GetMapping("/form/{id}")
	public String editarEmpleado(@PathVariable Long id, Map<String, Object> modelo, RedirectAttributes flash) {
		Empleado empleado = null;
		if (id > 0) {
			empleado = empleadoService.findOne(id);
			if (empleado == null) {
				flash.addFlashAttribute("error", "El id del empleado no existe en la BBDD");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del empleado no puede ser 0");
			return "redirect:/listar";
		}
		modelo.put("empleado", empleado);
		modelo.put("titulo", "Edición del empleado");
		return "formulario";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes flash) {
		if (id > 0) {
			empleadoService.delete(id);
			flash.addFlashAttribute("success", "Empleado eliminado con exito");
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/exportarPdf")
	private void expertarListadoEnPDF(HttpServletResponse response) {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
		String fechaActual = dateFormat.format(new Date());
		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Empleados_"+fechaActual+".pdf";
		response.setHeader(cabecera, valor);
		List<Empleado> empleados = empleadoService.findAll();
		EmpleadoExporterPDF exporter = new EmpleadoExporterPDF(empleados);
		try {
			exporter.exportar(response);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
}