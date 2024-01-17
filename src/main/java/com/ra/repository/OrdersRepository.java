package com.ra.repository;

import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Transactional
@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {

//    @Modifying
//    @Query("SELECT o FROM Orders o WHERE o.status = :status")
//    List<Orders> getAllByStatus(@Param("status") int status);

    List<Orders> findAllByStatus(int status);
    List<Orders> findByUser(User user);



//    OrdersResponseDTO findOrdersById(Long id);
}
