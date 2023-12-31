package com.bookshop.admin.payment

import com.bookshop.admin.order.Orders
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Connection

@RestController
@RequestMapping("/payment")
class PaymentController(private val paymentService: PaymentService) {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    // Bank 온라인 입금 정보를 리턴한다.
    @GetMapping("/bank-deposit")
    fun getCachedBankDeposit(): List<BankDepositResponse> {

        println("<<<<< PaymentController : Bank 온라인 입금 정보를 리턴 >>>>>>>>>")

        val REDIS_KEY = "bank-deposit"

        // Object <-> JSON
        val mapper = jacksonObjectMapper()

        val result = redisTemplate.opsForValue().get(REDIS_KEY)
        val bankDepositList: List<BankDepositResponse>

        if (result != null) {
            // value(json) -> List<BankDepositResponse>
            bankDepositList = mapper.readValue(result)
        } else {
            // 빈 리스트 초기화
            bankDepositList = emptyList()
        }

//        paymentService.updateOrdersStatus(bankDepositList)

        return bankDepositList
    }

    @PostMapping("/bank-deposit-backup")
    fun getCachedBankDeposit_backup(): ResponseEntity<Any> {
        val REDIS_KEY = "bank-deposit"

        // Object <-> JSON
        val mapper = jacksonObjectMapper()

        val result = redisTemplate.opsForValue().get(REDIS_KEY)
        val bankDepositList: List<BankDepositResponse>

        if (result != null) {
            // value(json) -> List<BankDepositResponse>
            bankDepositList = mapper.readValue(result)
        } else {
            // 빈 리스트 초기화
            bankDepositList = emptyList()
        }

        paymentService.updateOrdersStatus(bankDepositList)

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    // 결제방법이 온라인입금인 주문정보 조회
    @GetMapping("/orders/bank-deposit")
    fun fetchOrderInBankDeposit():
            List<BankDepositResponse> = transaction(Connection.TRANSACTION_READ_UNCOMMITTED, readOnly = true) {

        println("<<< PaymentService /payment/orders/bank-deposit >>>")

        // SQL
        // select id from orders where order_status = "0"
//                    .andWhere { (Orders.profileId eq authProfile.id) and (Orders.id eq orderId) }
        val result = transaction {
            Orders
                .slice(Orders.id)
                .select { Orders.orderStatus eq "0" }
                .map { row ->
                    BankDepositResponse(
                        orderId = row[Orders.id],
                        deposit = "Y",
                    )
                }
        }

        return@transaction result
    }



}