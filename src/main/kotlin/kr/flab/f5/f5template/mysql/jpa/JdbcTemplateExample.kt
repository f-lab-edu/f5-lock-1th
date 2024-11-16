package kr.flab.f5.f5template.mysql.jpa

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class JdbcTemplateExample(
    private val jdbcTemplate: JdbcTemplate,
) {

    fun selectAllProducts(): List<Product> {
        val sql = """
            SELECT
                id,
                name,
                price,
                stock
            FROM products
        """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            Product(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                price = rs.getLong("price"),
                stock = rs.getLong("stock"),
            )
        }
    }

    fun selectOneProduct(id: Long): Product? {
        val sql = """
            SELECT
                id,
                name,
                price,
                stock
            FROM products
            WHERE id = ?
        """.trimIndent()

        val args = listOf(id)

        return jdbcTemplate.query(
            sql, { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    name = rs.getString("name"),
                    price = rs.getLong("price"),
                    stock = rs.getLong("stock"),
                )
            },
            *args.toTypedArray()
        ).firstOrNull()
    }

    data class Product(
        val id: Long,
        val name: String,
        val price: Long,
        val stock: Long,
    )
}
