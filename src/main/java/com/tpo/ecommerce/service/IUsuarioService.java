package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.LoginRequestDTO;
import com.tpo.ecommerce.dto.UsuarioDTO;
import com.tpo.ecommerce.enums.UserRol;

import java.util.List;

public interface IUsuarioService {

    public List<UsuarioDTO> getUsuarios(Long id,UserRol userRol,String nombre,String mail,String apellido);
    public UsuarioDTO createUsuario(UsuarioDTO usuario);
    public UsuarioDTO register(UsuarioDTO usuario);
    public UsuarioDTO login(LoginRequestDTO loginRequest);
    public void deleteUsuario(Long id);
    public UsuarioDTO updateUsuario(Long id,UserRol userRol,String nombre,String mail,String apellido,String contrasenia);

}
