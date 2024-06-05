package com.hrs.infrastructure.persistence.shared;

import com.hrs.core.repository.shared.BaseRepository;
import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseRepositoryImpl<T, ID extends Serializable>
    implements BaseRepository<T, ID> {
  private final String ENTITY_ID = "id";
  private final String IS_ACTIVE = "isActive";
  private final String UPDATED_AT = "updatedAt";
  protected Class<T> entityClass;
  @PersistenceContext EntityManager entityManager;

  @SuppressWarnings("unchecked")
  public BaseRepositoryImpl(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  protected CriteriaBuilder getCriteriaBuilder() {
    return entityManager.getCriteriaBuilder();
  }

  protected CriteriaQuery<T> createCriteriaQuery() {
    return getCriteriaBuilder().createQuery(entityClass);
  }

  protected Root<T> getRoot(CriteriaQuery<T> cq) {
    return cq.from(entityClass);
  }

  @Override
  public T findById(Long id) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery cq = createCriteriaQuery();
    Root<T> root = getRoot(cq);
    Predicate predicate = cb.equal(root.get(ENTITY_ID), id);
    cq.where(predicate);
    try {
      return (T) entityManager.createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null; // Or throw a custom exception if needed
    }
  }

  @Override
  @Transactional
  public void persist(T t) {
    entityManager.persist(t);
  }

  @Override
  @Transactional
  public void persistAndFlush(T t) {
    entityManager.persist(t);
    entityManager.flush();
  }

  @Override
  @Transactional
  public void merge(T t) {
    entityManager.merge(t);
  }

  @Override
  public List<T> findAll() {
    CriteriaQuery cq = createCriteriaQuery();
    try {
      return entityManager.createQuery(cq).getResultList();
    } catch (NoResultException e) {
      return Collections.emptyList(); // Or throw a custom exception if needed
    }
  }

  @Override
  public T getSingleResult(String param, String value) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaQuery cq = createCriteriaQuery();
    Root<T> root = getRoot(cq);
    Predicate predicate = cb.equal(root.get(param), value);
    cq.where(predicate);
    try {
      return (T) entityManager.createQuery(cq).getSingleResult();
    } catch (NoResultException e) {
      return null; // Or throw a custom exception if needed
    }
  }

  @Override
  @Transactional
  public void softDelete(Long id) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaUpdate<T> delete = cb.createCriteriaUpdate(entityClass);
    Root<T> root = delete.from(entityClass);
    delete.where(cb.equal(root.get(ENTITY_ID), id)); // Assuming "id" is the ID field
    delete.where(cb.equal(root.get(IS_ACTIVE), true)); // Assuming "id" is the ID field
    delete.set(IS_ACTIVE, false);
    delete.set(UPDATED_AT, Instant.now().toEpochMilli());
    entityManager.createQuery(delete).executeUpdate();
  }

  @Override
  @Transactional
  public void hardDelete(Long id) {
    CriteriaBuilder cb = getCriteriaBuilder();
    CriteriaDelete<T> delete = cb.createCriteriaDelete(entityClass);
    Root<T> root = delete.from(entityClass);
    delete.where(cb.equal(root.get(ENTITY_ID), id)); // Assuming "id" is the ID field
    delete.where(cb.equal(root.get(IS_ACTIVE), true)); // Assuming "id" is the ID field
    entityManager.createQuery(delete).executeUpdate();
  }
}
