package com.teatro.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.teatro.model.Usuario;

/**
 * Repository para operações de persistência da entidade Usuario
 * 
 * Fornece métodos para: - Autenticação por CPF ou email - Busca por diferentes critérios -
 * Validação de unicidade
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por CPF
     */
    Optional<Usuario> findByCpf(String cpf);

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuário por CPF ou email (para autenticação)
     */
    @Query("SELECT u FROM Usuario u WHERE u.cpf = :identificador OR u.email = :identificador")
    Optional<Usuario> findByCpfOrEmail(@Param("identificador") String identificador);

    /**
     * Verifica se existe usuário com CPF
     */
    boolean existsByCpf(String cpf);

    /**
     * Verifica se existe usuário com email
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuários ativos
     */
    List<Usuario> findByAtivoTrue();

    /**
     * Busca usuários por tipo
     */
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    /**
     * Busca usuários ativos por tipo
     */
    List<Usuario> findByAtivoTrueAndTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    /**
     * Busca usuários por nome (contendo)
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca usuários ativos por nome (contendo)
     */
    List<Usuario> findByAtivoTrueAndNomeContainingIgnoreCase(String nome);

    /**
     * Conta usuários ativos
     */
    long countByAtivoTrue();

    /**
     * Conta usuários por tipo
     */
    long countByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    /**
     * Busca usuário por CPF e email (para validação de recuperação de senha)
     */
    Optional<Usuario> findByCpfAndEmail(String cpf, String email);
}
