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

    @Column(name = "price")
    var price: Long = price

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

    fun decreaseStock() {
        if (stock <= 0) {
            throw IllegalArgumentException("No stock for product $id")
        }
        stock -= 1
    }

    fun increaseStock() {
        stock += 1
    }

    fun updateStock(amount: Long) {
        if (stock + amount < 0) {
            throw IllegalArgumentException("There is not enough stock to decrease")
        }
        stock += amount
    }
}
