package com.projectwm.api.controller

import com.projectwm.api.models.ProductoLocal
import com.projectwm.api.models.UsuarioLocal
import com.projectwm.api.requests.UserRequest
import com.projectwm.api.services.ProductServiceImpl
import com.projectwm.api.services.UserServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api", produces = [MediaType.APPLICATION_JSON_VALUE])
class Controller {

    private val userService = UserServiceImpl()
    private val productService = ProductServiceImpl()

    @CrossOrigin(origins = ["*"])
    @GetMapping("/users/{email}")
    @ResponseBody
    suspend fun getByEmail(@PathVariable email: String) =
        ResponseEntity(userService.getByEmail(email), HttpStatus.OK)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/users")
    suspend fun paginatedUserList(@RequestParam pageNo: Int, @RequestParam pageSize: Int,@RequestParam by: String) =
        userService.getPageBy(pageNo, pageSize, by)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/initUsers")
    suspend fun getAllUsers() = userService.getAll()

    @CrossOrigin(origins = ["*"])
    @PatchMapping("/users")
    suspend fun updateUser(@RequestBody @Valid user: UserRequest) =
        userService.updateUser(user)

    @CrossOrigin(origins = ["*"])
    @PostMapping("/users")
    suspend fun createUser(@RequestBody @Valid user: UsuarioLocal) =
        userService.createUser(user)

    @CrossOrigin(origins = ["*"])
    @DeleteMapping("/users/{email}")
    suspend fun deleteUser(@PathVariable email: String) =
        userService.deleteUser(email)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/prodByTitle/{title}")
    suspend fun getByTitle(@PathVariable title: String) =
        ResponseEntity(productService.getByTitle(title), HttpStatus.OK)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/products")
    suspend fun paginatedProductList(@RequestParam pageNo: Int, @RequestParam pageSize: Int) =
        productService.getPageBy(pageNo, pageSize)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/prodByCompany/{company}")
    suspend fun getByCompany(@PathVariable company: String) = productService.getByCompany(company)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/prodById/{id}")
    @ResponseBody
    suspend fun getById(@PathVariable id: Int) =
        ResponseEntity(productService.getById(id), HttpStatus.OK)

    @CrossOrigin(origins = ["*"])
    @GetMapping("/initProducts")
    suspend fun getAllProducts() = productService.getAllProducts()

    @CrossOrigin(origins = ["*"])
    @PatchMapping("/products")
    suspend fun updateProd(@RequestBody @Valid product: ProductoLocal) =
        productService.updateProduct(product)

    @CrossOrigin(origins = ["*"])
    @PostMapping("/products")
    suspend fun createProduct(@RequestBody @Valid product: ProductoLocal) =
        productService.createProduct(product)

    @CrossOrigin(origins = ["*"])
    @DeleteMapping("/products/{id}")
    suspend fun deleteProduct(@PathVariable id: Int) =
        productService.deleteProduct(id)
}