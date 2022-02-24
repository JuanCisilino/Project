package com.projectwm.api.databases

import com.projectwm.api.models.ProductoLocal
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Producto: Table("productos") {
    val id = integer("id")
    val titulo = varchar("title", 400)
    val descripcion = varchar("description", 3000)
    val precio = double("cost")
    val stock = integer("stock")
    val imagen = varchar("image", 300000)
    val empresa = varchar("company", 1000)
    val tipo = varchar("type", 60)

    fun getAll(): List<ProductoLocal> = transaction { Producto.selectAll().map { convertToProducto(it) }.sortedBy { it.id } }

    private fun convertToProducto(row: ResultRow?): ProductoLocal {
        return ProductoLocal(
            id = row?.get(id)?:0,
            title = row?.get(titulo),
            description = row?.get(descripcion),
            cost = row?.get(precio),
            stock = row?.get(stock)?:0,
            image = row?.get(imagen),
            company = row?.get(empresa),
            type = row?.get(tipo)
        )
    }

    fun getById(id: Int) = getAll().find { it.id == id }

    fun getByCompany(company: String) = getAll().filter { it.company == company }.sortedBy { it.id }

    fun getByTitle(title: String) = getAll().filter { it.title?.contains(title)?: false }

    fun getPageBy(limit: Int, offset: Int): List<ProductoLocal> = transaction {
        val list = Producto.selectAll()
            .map { convertToProducto(it) }
            .sortedBy { it.company }
        return@transaction list.take(offset).takeLast(limit)
    }
}