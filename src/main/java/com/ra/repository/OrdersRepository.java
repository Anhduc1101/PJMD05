package com.ra.repository;

import com.ra.model.entity.Orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {
//    @Modifying
//    @Query("update Orders o set o.status = :status where o.id = :id")
//    void changeStatus(@Param("id") Long id, @Param("newStatus") int status);
}
