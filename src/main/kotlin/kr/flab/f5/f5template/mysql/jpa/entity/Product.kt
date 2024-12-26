package kr.flab.f5.f5template.mysql.jpa.entity

import kr.flab.f5.f5template.exception.ApiException
import kr.flab.f5.f5template.exception.ErrorType
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.http.HttpStatus
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
    createdAt: Instant = Instant.now(),
    updatedAt: Instant = Instant.now(),
    stock: Long,
) {

    init {
        if (name.isEmpty()) {
            throw ApiException("상품명을 입력하지 않았습니다.", ErrorType.INVALID_PARAMETER, HttpStatus.BAD_REQUEST)
        }
    }

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


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant = createdAt
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = updatedAt
        private set

    @Column(name = "stock", nullable = false)
    var stock: Long = stock
        private set

    fun reviseProduct(name: String, price: Long) {
        this.name = name
        this.price = price
    }

    fun increaseStock(amount: Long) {
        if (amount <= 0) {
            throw IllegalArgumentException("Increase amount must be positive.")
        }
        stock += amount
    }

    fun modifyStock(newQuantity: Long) : Long {
        val stockDifference = newQuantity - stock
        when {
            stockDifference > 0 -> {
                increaseStock(stockDifference)
            }
            stockDifference < 0 -> {
                decreaseStock(stockDifference)
            }
        }
        return stock
    }

    fun decreaseStock(substractStock: Long) {
        if (substractStock <= 0) {
            throw IllegalArgumentException("Decrease amount must be positive.")
        }
        if (stock < substractStock) {
            throw IllegalArgumentException("Not enough stock to decrease.")
        }
        stock -= substractStock
    }
}
