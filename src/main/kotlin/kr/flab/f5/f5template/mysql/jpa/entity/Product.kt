package kr.flab.f5.f5template.mysql.jpa.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "product")
@Entity
class Product(
    id: Long = 0,
    name: String,
    price: Long,
    stock: Long,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        private set

    @Column(name = "name")
    var name: String = name
        private set

    @Column(name = "price")
    var price: Long = price
        private set

    @Column(name = "stock")
    var stock: Long = stock
        private set

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant = createdAt
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = updatedAt
        private set

    fun decreaseStock(amount: Long = 1) {
        if (stock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        if (stock < amount) {
            throw IllegalArgumentException("Not enough stock for product $id")
        }
        stock -= amount
    }
}
