package com.projectwm.api.services

import com.projectwm.api.databases.DatabaseConnection
import com.projectwm.api.responses.PagingResponse
import com.projectwm.api.databases.Producto
import com.projectwm.api.models.ProductoLocal
import com.projectwm.api.requests.ProductRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.sql.SQLException

class ProductServiceImpl {

    fun getByTitle(title: String)= try {
        ResponseEntity(Producto.getByTitle(title), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    fun getByCompany(company: String)= try {
        ResponseEntity(Producto.getByCompany(company), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    fun getById(id: Int)= try {
        ResponseEntity(Producto.getById(id), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    fun getAllProducts()= try {
        ResponseEntity(Producto.getAll(), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    fun getPageBy(pageNo: Int, pageSize: Int): PagingResponse<ProductoLocal> {
        val offset = pageSize * pageNo
        val list = Producto.getPageBy(pageSize, offset)
        val total = Producto.getAll().size.toLong()
        return PagingResponse(total, list)
    }

    fun createProduct(product: ProductoLocal) = try {
        transaction {
            Producto.insert {
                it[id] = product.id
                it[titulo] = product.title ?: "none"
                it[descripcion] = product.description ?: "none"
                it[empresa] = product.company ?: "none"
                it[stock] = product.stock
                it[tipo] = product.type ?: "none"
                it[imagen] = product.image ?: "none"
                it[precio] = product.cost?: 0.0
            }
        }
        ResponseEntity(Producto.getById(product.id), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    fun deleteProduct(id: Int)  = try {
        val sql = "DELETE FROM productos WHERE id = '$id'"
        DatabaseConnection().connect().use { conn ->
            conn?.prepareStatement(sql).use { pstmt -> pstmt?.executeUpdate() }
        }
        ResponseEntity("Producto eliminado", HttpStatus.OK)
    } catch (ex: SQLException) {
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    fun updateProduct(product: ProductRequest): ResponseEntity<Any> {
        getString(product)?.let {
            val sql = it
            return try {
                DatabaseConnection().connect().use { conn ->
                    conn?.prepareStatement(sql).use { pstmt -> pstmt?.executeUpdate() }
                }
                ResponseEntity(Producto.getById(product.id), HttpStatus.OK)
            } catch (ex: SQLException) {
                ResponseEntity(product.title, HttpStatus.NOT_MODIFIED)
            }
        }
            ?:run { return ResponseEntity(product.title, HttpStatus.NOT_MODIFIED) }
    }

    private fun getString(product: ProductRequest): String? {
        product.title?.let { return "UPDATE productos SET title = '${it}' WHERE id = '${product.id}'" }
        product.description?.let { return "UPDATE productos SET description = '${it}' WHERE id = '${product.id}'" }
        product.cost?.let { return "UPDATE productos SET cost = '${it}' WHERE id = '${product.id}'" }
        product.image?.let { return "UPDATE productos SET image = '${it}' WHERE id = '${product.id}'" }
        product.stock?.let { return "UPDATE productos SET stock = '${it}' WHERE id = '${product.id}'" }
        product.type?.let { return "UPDATE productos SET type = '${it}' WHERE id = '${product.id}'" }
        return null
    }
}
