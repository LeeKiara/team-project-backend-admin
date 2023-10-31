package com.example.sales

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class OrderResponse(
    val itemId: Int,
    val orderItems: MutableList<OrderItemsResponse>
)
data class OrderItemsResponse(
    val title: String,
    val cover: String
)


@RestController
@RequestMapping("/orders")
class OrderProductController {


    val orderProducts = listOf(
        OrderResponse(10000, mutableListOf(OrderItemsResponse("title1","http://.../1.png"))),
        OrderResponse(20000, mutableListOf(OrderItemsResponse("title2","http://.../2.png"))),
        OrderResponse(30000, mutableListOf(OrderItemsResponse("title3","http://.../3.png"))),
        OrderResponse(40000, mutableListOf(OrderItemsResponse("title4","http://.../4.png"))),
        OrderResponse(50000, mutableListOf(OrderItemsResponse("title5","http://.../5.png"))),
    )


    @RequestMapping("/products")
    fun getOrdersProducts() : List<OrderResponse> {
        return orderProducts;
    }

}