package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.UsuarioResponseDTO;
import com.tpo.ecommerce.enums.UserRol;

import java.util.List;

public interface IUsuarioService {

    public List<UsuarioResponseDTO> getUsuarios(Long id,UserRol userRol,String nombre,String mail,String apellido);
    public void deleteUsuario(Long id);
    public void eliminarLogico(Long id);
    public UsuarioResponseDTO updateUsuario(Long id,UserRol userRol,String nombre,String mail,String apellido,String contrasenia);

}
