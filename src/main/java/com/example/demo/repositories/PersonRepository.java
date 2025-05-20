package com.example.demo.repositories;

import com.example.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    //define custom query using JPQL with index parameters
    @Query("select p from Person p where p.firstNome =?1 and p.lastNome =?2")
    Person findByJPQL(String firstNome, String lastNome);

    //define custom query using JPQL with named parameters
    @Query("select p from Person p where p.firstNome =:firstNome and p.lastNome =:lastNome")
    Person findByJPQLNamedParameters(@Param("firstNome") String firstNome,
                                     @Param("lastNome") String lastNome);

    //define custom query using native sql with index parameters
    @Query(value = "select * from pessoa p where p.first_name =?1 and p.last_name =?2", nativeQuery = true)
    Person findByNativeSQL(String firstNome, String lastNome);

    //define custom query using native sql with named parameters
    @Query(value = "select * from pessoa p where p.first_name =:firstNome and p.last_name =:lastNome", nativeQuery = true)
    Person findByNativeSQLNamedParameters(@Param("firstNome") String firstNome,
                                          @Param("lastNome") String lastNome);
}
