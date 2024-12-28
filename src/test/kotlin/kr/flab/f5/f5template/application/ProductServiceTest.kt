
import kr.flab.f5.f5template.F5LockApplication
import kr.flab.f5.f5template.lecture.week4.mission.DecreaseStock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest(classes = [F5LockApplication::class])
class ProductServiceTest {
    private val count = 5
    private val id = 3L

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var decreaseStock: DecreaseStock


    private val executors = Executors.newFixedThreadPool(count)

    @BeforeEach
    fun setUp() {
        jdbcTemplate.update("UPDATE product SET stock = 100 WHERE id = ?", id)
    }

    @Test
    fun `동시성 테스트`() {
        val id = 3L
        val n = 20
        val initStock = jdbcTemplate.queryForObject("SELECT stock FROM product WHERE id = ?", { rs, rowNum -> rs.getLong("stock") }, id)!!
        val countDownLatch = CountDownLatch(n)

        repeat(n) {
            executors.submit {
                decreaseStock.execute(id)
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()
        val expectedStock = initStock - n
        val decreasedStock = jdbcTemplate.queryForObject("SELECT stock FROM product WHERE id = ?", { rs, _ -> rs.getLong("stock") }, id)
        println("initStock: $initStock expectedStock: $expectedStock, decreasedStock: $decreasedStock, count: $n")
        assertThat(decreasedStock).isEqualTo(expectedStock)
    }
}
