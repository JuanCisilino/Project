package com.projectwm.api.databases

import com.projectwm.api.models.UsuarioLocal
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Usuario: Table(name = "usuarios") {
    val email = varchar("email", 100)
    val nombre = varchar("nombre", 60)
    val rol = varchar("rol", 30)
    val carrito = varchar("cart", 30000)
    val empresa = varchar("empresa", 100)

    fun getAll(): List<UsuarioLocal> = transaction { Usuario.selectAll().map { convertToUsuario(it) }.sortedBy { it.rol } }

    private fun convertToUsuario(row: ResultRow?): UsuarioLocal {
        return UsuarioLocal(
            email = row?.get(email)?:"undefined",
            nombre = row?.get(nombre),
            rol = row?.get(rol),
            cart = row?.get(carrito),
            empresa = row?.get(empresa)
        )
    }

    fun getByEmail(email: String) = getAll().find { it.email == email }

    fun getByName(name: String) = getAll().find { it.nombre == name }

    fun getByRol(rol: String) = getAll().filter { it.rol == rol }

    fun getByCompany(company: String) = getAll().filter { it.empresa == company }

    fun getPageBy(limit: Int, offset: Int, by: String): List<UsuarioLocal> = transaction {
        val list = Usuario.selectAll()
            .map { convertToUsuario(it) }
            .sortedBy {
                when (by){
                    "rol" -> it.rol
                    "nombre" -> it.nombre
                    "empresa" -> it.empresa
                    else -> it.email
                }
            }
        return@transaction list.take(offset).takeLast(limit)
    }

}