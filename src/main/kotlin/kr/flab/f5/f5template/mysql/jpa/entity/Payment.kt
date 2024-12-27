package kr.flab.f5.f5template.mysql.jpa.entity

import org.hibernate.annotations.CreationTimestamp
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

@Table(name = "payment")
@Entity
class Payment(
    id: Long = 0,
    orderId: Long,
    status: PaymentStatus = PaymentStatus.READY,
    method: PaymentMethod,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        private set

    @Column(name = "order_id")
    var orderId: Long = orderId
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: PaymentStatus = status
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    var method: PaymentMethod = method
        private set

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant = createdAt
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = updatedAt
        private set

    fun complete() {
        if (status != PaymentStatus.READY) {
            throw IllegalArgumentException("대기 상태인 결제만 완료 처리할 수 있습니다.")
        }
        status = PaymentStatus.COMPLETED
    }
}
