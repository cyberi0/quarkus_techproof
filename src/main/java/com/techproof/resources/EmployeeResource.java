package com.techproof.resources;

import com.techproof.entity.Employee;
import com.techproof.entity.EmployeeWorkedHours;
import com.techproof.repository.EmployeeWorkedHoursRepository;
import com.techproof.requests.JobRequest;
import com.techproof.services.EmployeeService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.techproof.repository.EmployeeRepository;
import jakarta.ws.rs.core.Response;

@Path("/employees")
public class EmployeeResource {

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    EmployeeService employeeService;

    @Inject
    EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

    @PersistenceContext
    EntityManager entityManager;

    @POST
    @Transactional
    public Response createEmployee(Employee employee) {
        if (employee.getGender() == null || employee.getJob() == null || employee.getName() == null || employee.getLastName() == null || employee.getBirthDate() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Enviar Todos los Datos Requeridos")
                    .build();
        }

        try {
            employee.persist();
            return Response.status(Response.Status.CREATED)
                    .entity(buildResponseJson(employee.getId(), true))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(buildResponseJson(null, false))
                    .build();
        }
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getAllEmployees() {
        return employeeRepository.listAll();
    }

    @POST
    @Path("/byJobId")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployeesByJobId(JobRequest jobRequest) {
        if (jobRequest == null || jobRequest.getJobId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Job ID is required").build();
        }

        Long jobId = jobRequest.getJobId();

        List<Employee> employees = employeeService.getEmployeesByJobId(jobId);
        List<Employee> filteredEmployees = employeeService.filterAndSortEmployeesByJobId(jobId, employees);
        Map<String, List<Employee>> groupedEmployees = employeeService.groupEmployeesByLastName(filteredEmployees);

        return Response.ok(groupedEmployees).build();
    }

    private JsonObject buildResponseJson(Long id, boolean success) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder = (id != null) ? builder.add("id", id) : builder.addNull("id");
        builder.add("success", success);
        JsonObject responseJson = builder.build();
        return responseJson;
    }


    @GET
    @Path("/multiTread")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployeesMultiThreaded() throws InterruptedException, ExecutionException {
        List<Employee> result = new ArrayList<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        List<Employee> employees = getAllEmployees();
        for (Employee employee : employees) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                result.add(employee);
            });
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

        return result;
    }


    @POST
    @Path("/countWorkHours")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalWorkHours(JsonObject input) {
        Long employeeId;
        LocalDate startDate, endDate;
        try {
            employeeId = input.getJsonNumber("employee_id").longValue();
            startDate = LocalDate.parse(input.getString("start_date"));
            endDate = LocalDate.parse(input.getString("end_date"));
        } catch (DateTimeParseException | ClassCastException | NullPointerException | IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de JSON de entrada incorrecto o valores inválidos.")
                    .build();
        }

        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Empleado no encontrado con el ID: " + employeeId)
                    .build();
        }

        if (startDate.isAfter(endDate)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La fecha de inicio debe ser anterior a la fecha de fin.")
                    .build();
        }

        JsonObject responseJson = calculateTotalWorkHours(employeeId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        return Response.ok()
                .entity(responseJson.toString())
                .build();
    }

    private JsonObject calculateTotalWorkHours(Long employeeId, LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        Long totalHoursWorked = (Long) entityManager.createQuery(
            "SELECT  coalesce(sum(e.workedHours), 0) " +
                "FROM EmployeeWorkedHours e " +
                "WHERE e.employee.id = :employeeId " +
                "AND e.workedDate >= :startDate " +
                "AND e.workedDate <= :endDate")
                .setParameter("employeeId", employeeId)
                .setParameter("startDate", startTimestamp)
                .setParameter("endDate", endTimestamp)
                .getSingleResult();

        JsonObject response = Json.createObjectBuilder()
                .add("employee_id", employeeId)
                .add("total_hours_worked", totalHoursWorked)
                .add("success", true)
                .build();

        return response;
    }

    @POST
    @Path("/payWorkHours")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalPayment(JsonObject input) {
        Long employeeId;
        LocalDate startDate, endDate;
        try {
            employeeId = input.getJsonNumber("employee_id").longValue();
            startDate = LocalDate.parse(input.getString("start_date"));
            endDate = LocalDate.parse(input.getString("end_date"));
        } catch (DateTimeParseException | ClassCastException | NullPointerException | IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Verificar los datos.")
                    .build();
        }

        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Empleado no encontrado ID: " + employeeId)
                    .build();
        }

        if (startDate.isAfter(endDate)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La fecha de inicio debe ser anterior a la fecha de fin.")
                    .build();
        }

        JsonObject responseJson = calculateTotalPayWork(employeeId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        return Response.ok()
                .entity(responseJson.toString())
                .build();
    }

    public JsonObject calculateTotalPayWork(Long employeeId, LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        Object[] result = (Object[]) entityManager.createQuery(
                "SELECT COALESCE(SUM(e.workedHours), 0), " +
                        "COALESCE(SUM(e.workedHours * j.salary), 0.0) " +
                        "FROM EmployeeWorkedHours e " +
                        "JOIN e.employee.job j " +
                        "WHERE e.employee.id = :employeeId " +
                        "AND e.workedDate >= :startDate " +
                        "AND e.workedDate <= :endDate")
                .setParameter("employeeId", employeeId)
                .setParameter("startDate", startTimestamp)
                .setParameter("endDate", endTimestamp)
                .getSingleResult();

        Long totalHoursWorked = ((Number) result[0]).longValue();
        Double totalPay = ((Number) result[1]).doubleValue();

        JsonObject response = Json.createObjectBuilder()
                .add("payment", totalPay)
                .add("success", true)
                .build();
        return response;
    }


}
