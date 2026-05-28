package edu.touro.mcon364.finalreview.treesandthreads.exercises;

import edu.touro.mcon364.finalreview.treesandthreads.model.Employee;

import java.util.*;
import java.util.stream.*;
import java.util.Map;

/**
 * In-class Exercise 2 — Employee Roster (TreeMap + TreeSet + Streams)
 *
 * Scenario: a company has employees spread across several departments.
 * You need to organize them so that departments are listed alphabetically
 * and employees within each department are also sorted alphabetically by name.
 *
 * This exercise practises:
 * - TreeMap<String, TreeSet<Employee>> as a two-level sorted structure.
 * - Collectors.groupingBy with a TreeSet downstream collector.
 * - Stream flatMap to merge employees from all departments into one list.
 * - NavigableMap range queries (departments A-M vs N-Z).
 *
 * Before coding, think about:
 * - Why do we use TreeSet inside the map rather than ArrayList?
 * - What comparator drives the ordering inside each TreeSet?
 * - If two employees have the same name and department but different salaries,
 *   are they considered the same element inside the TreeSet?
 *
 * Requirements:
 * - The constructor receives the list of employees.
 * - buildRoster() returns a TreeMap<String, TreeSet<Employee>> grouping
 *   employees by department. Both the departments and the employees within
 *   each department must be in sorted order.
 * - getTopEarnerPerDepartment() returns a Map<String, Employee> with the
 *   highest-paid employee in each department.
 * - getAllEmployeesSorted() returns all employees across all departments in
 *   a single sorted list (alphabetical by name).
 * - getDepartmentsInRange(from, to) returns a NavigableMap slice containing
 *   only the departments whose names fall in [from, to] inclusive.
 *
 * Do not use explicit loops. Use streams and collectors.
 */
public class EmployeeRoster {

    private final List<Employee> employees;

    public EmployeeRoster(List<Employee> employees) {
        // validate non-null, store a defensive copy
        if (employees == null) {
            throw new IllegalArgumentException("employees cannot be null");
        }
        this.employees = List.copyOf(employees);
    }

    /**
     * Groups employees by department into a sorted two-level structure.
     *
     * @return sorted map: department name -> sorted set of employees
     */
    public TreeMap<String, TreeSet<Employee>> buildRoster() {
        if (employees.isEmpty()) {
            return new TreeMap<>();
        }
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        TreeMap::new,
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Employee::name)))
                ));
    }

    /**
     * Returns the highest-paid employee in each department.
     *
     * @return map of department name -> top earner
     */
    public Map<String, Employee> getTopEarnerPerDepartment() {
        if (employees.isEmpty()) {
            return new HashMap<>();
        }
        TreeMap<String, TreeSet<Employee>> roster = buildRoster();
        TreeMap<String, Employee> topEarner = new TreeMap<>();
        roster.entrySet().stream()
                .forEach((e) -> {
                    Employee top = e.getValue().stream()
                            .max(Comparator.comparing(Employee::salary))
                            .orElse(null);
                    topEarner.put(e.getKey(), top);
                });

        return topEarner;
    }

    /**
     * Returns every employee across all departments in a single alphabetical list.
     *
     *
     * @return globally sorted employee list
     */
    public List<Employee> getAllEmployeesSorted() {
        return employees.stream()
                .sorted(Comparator.comparing(Employee::name))
                .toList();
    }

    /**
     * Returns a view of the roster containing only departments in [from, to].
     *
     *
     * @param from lower bound department name (inclusive)
     * @param to   upper bound department name (inclusive)
     * @return navigable sub-map
     */
    public NavigableMap<String, TreeSet<Employee>> getDepartmentsInRange(String from, String to) {
        TreeMap<String, TreeSet<Employee>> roster = buildRoster();
        return roster.subMap(from, true, to, true);
    }
}

