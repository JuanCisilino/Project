package com.projectwm.api.services

import com.projectwm.api.databases.DatabaseConnection
import com.projectwm.api.responses.PagingResponse
import com.projectwm.api.databases.Usuario
import com.projectwm.api.models.UsuarioLocal
import com.projectwm.api.requests.UserRequest
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.sql.SQLException

class UserServiceImpl {

    fun getByEmail(email: String) = try {
        ResponseEntity(Usuario.getByEmail(email), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    fun getPageBy(pageNo: Int, pageSize: Int, by: String): PagingResponse<UsuarioLocal> {
        val offset = pageSize * pageNo
        val list = Usuario.getPageBy(pageSize, offset, by)
        val total = Usuario.getAll().size.toLong()
        return PagingResponse(total, list)
    }

    fun getAll() = try {
        ResponseEntity(Usuario.getAll(), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.I_AM_A_TEAPOT)
    }

    fun updateUser(user: UserRequest): ResponseEntity<Any> {
        val sql = "UPDATE usuarios SET rol = '${user.rol}' WHERE email = '${user.email}'"
        return try {
            DatabaseConnection().connect().use { conn ->
                conn?.prepareStatement(sql).use { pstmt -> pstmt?.executeUpdate() }
            }
            ResponseEntity(Usuario.getByEmail(user.email), HttpStatus.OK)
        } catch (ex: SQLException) {
            ResponseEntity(user.email, HttpStatus.NOT_MODIFIED)
        }
    }

    fun createUser(user: UsuarioLocal) = try {
        transaction {
            Usuario.insert {
                it[email] = user.email
                it[nombre] = user.nombre ?: "none"
                it[rol] = user.rol ?: "none"
                it[empresa] = user.empresa ?: "none"
            }
        }
        ResponseEntity(Usuario.getByEmail(user.email), HttpStatus.OK)
    } catch (ex: SQLException){
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    fun deleteUser(email: String)  = try {
        val sql = "DELETE FROM usuarios WHERE email = '$email'"
        DatabaseConnection().connect().use { conn ->
            conn?.prepareStatement(sql).use { pstmt -> pstmt?.executeUpdate() }
        }
        ResponseEntity("Usuario eliminado", HttpStatus.OK)
    } catch (ex: SQLException) {
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }
}
