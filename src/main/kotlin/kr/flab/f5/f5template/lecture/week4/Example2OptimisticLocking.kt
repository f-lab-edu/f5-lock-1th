package kr.flab.f5.f5template.lecture.week4

import kr.flab.f5.f5template.mysql.jpa.entity.OrderStatus
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OptimisticLockType
import org.hibernate.annotations.OptimisticLocking
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@OptimisticLocking(type = OptimisticLockType.VERSION)
@Table(name = "order")
@Entity
class Example2OptimisticLocking(
    id: Long = 0,
    productId: Long,
    totalAmount: Long,
    status: OrderStatus = OrderStatus.READY,
    version: Long = 0,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        private set

    @Column(name = "product_id")
    var productId: Long = productId
        private set

    @Column(name = "total_amount")
    var totalAmount: Long = totalAmount
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: OrderStatus = status
        private set

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant = createdAt
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = updatedAt
        private set

    @Version
    var version: Long = version

    fun complete() {
        if (status != OrderStatus.READY) {
            throw IllegalArgumentException("준비 상태인 주문만 완료 처리할 수 있습니다.")
        }
        status = OrderStatus.COMPLETED
    }
}
