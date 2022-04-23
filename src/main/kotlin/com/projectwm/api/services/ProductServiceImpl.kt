package com.projectwm.api.services

import com.projectwm.api.databases.DatabaseConnection
import com.projectwm.api.responses.PagingResponse
import com.projectwm.api.databases.Producto
import com.projectwm.api.databases.Producto.id
import com.projectwm.api.models.ProductoLocal
import com.projectwm.api.requests.ProductRequest
import org.jetbrains.exposed.sql.deleteWhere
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
        val randomId = getRandomId()
        transaction { generateProduct(randomId, product) }
        ResponseEntity(Producto.getById(randomId), HttpStatus.OK)
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

    fun updateProduct(product: ProductoLocal): ResponseEntity<Any> {
        val sql = "DELETE FROM productos WHERE id = '${product.id}'"
        DatabaseConnection().connect().use { conn ->
            conn?.prepareStatement(sql).use { pstmt -> pstmt?.executeUpdate() }
        }
        return try {
            transaction { generateProduct(product.id, product) }
            ResponseEntity(Producto.getById(product.id), HttpStatus.OK)
        } catch (ex: SQLException) {
            ResponseEntity(product.title, HttpStatus.NOT_MODIFIED)
        }
    }

    private fun generateProduct(id: Int, product: ProductoLocal){
        Producto.insert {
            it[Producto.id] = id
            it[titulo] = product.title ?: ""
            it[descripcion] = product.description ?: ""
            it[empresa] = product.company ?: ""
            it[stock] = product.stock
            it[tipo] = product.type ?: ""
            it[imagen] = product.image ?: ""
            it[activo] = product.isActive
            it[precio] = product.cost?: 0.0
        }
    }

    private fun getRandomId(): Int {
        val randomId = generateId()
        val existingUser = Producto.getById(randomId)
        existingUser?.let { getRandomId() }
        return randomId
    }

    private fun generateId(): Int {
        require(1 <= 9999) { "0" }
        return (1..9999).random()
    }
}
