package kr.flab.f5.f5template.mysql.jpa.repository

import kr.flab.f5.f5template.mysql.jpa.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>
