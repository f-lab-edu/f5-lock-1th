package kr.flab.f5.f5template.lecture.week4

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OptimisticLockType
import org.hibernate.annotations.OptimisticLocking
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version

@OptimisticLocking(type = OptimisticLockType.VERSION)
@Entity
@Table(name="product")
class ProductOptimisticLocking(
    id: Long = 0,
    name: String,
    price: Long,
    stock: Long,
    version: Long = 0,
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

    @Version
    var version: Long = version

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

    fun updateProduct(product: ProductOptimisticLocking) {
        name = product.name
        price = product.price
        stock = product.stock
    }
}