package com.example.spring_boot_testing.repository;

import com.example.spring_boot_testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    // define custom query using JPQL with index params (?n)
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQLIndexParams(String firstName, String lastNAme);

    // define custom query using JPQL with named params
    @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastNAme);

    // define custom query using native SQL with index params
    @Query(value = "select * from employees e where e.first_Name = ?1 and e.last_Name = ?2", nativeQuery = true)
    Employee findByNativeSQLIndexParams(String firstName, String lastName);

    // define custom query using native SQL with named params
    @Query(value = "select * from employees e where e.first_Name =:firstName and e.last_Name =:lastName", nativeQuery = true)
    Employee findByNativeSQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
