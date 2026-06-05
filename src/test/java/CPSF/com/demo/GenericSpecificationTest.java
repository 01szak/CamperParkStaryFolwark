package CPSF.com.demo;

import CPSF.com.demo.model.constant.Operation;
import CPSF.com.demo.service.core.GenericSpecification;
import CPSF.com.demo.service.core.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GenericSpecificationTest {

    @Mock
    private Root<Object> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder cb;
    @Mock
    private Path<Object> path;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(root.get(anyString())).thenReturn(path);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldCreateEqualPredicateForString() {
        // given
        var criteria = new SearchCriteria("name", Operation.EQUALS, "Kowalski");
        var spec = new GenericSpecification<>(criteria);
        when(path.getJavaType()).thenReturn((Class) String.class);

        // when
        spec.toPredicate(root, query, cb);

        // then
        verify(cb).equal(path, "Kowalski");
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldCreateBetweenPredicateForLocalDate() {
        // given
        var criteria = new SearchCriteria("checkin", Operation.BETWEEN, "2026-05-01", "2026-05-10");
        var spec = new GenericSpecification<>(criteria);
        when(path.getJavaType()).thenReturn((Class) LocalDate.class);

        // when
        spec.toPredicate(root, query, cb);

        // then
        verify(cb).between(any(), eq(LocalDate.of(2026, 5, 1)), eq(LocalDate.of(2026, 5, 10)));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldCorrectyParseBigDecimal() {
        // given
        var criteria = new SearchCriteria("price", Operation.GREATER_THEN, "150.50");
        var spec = new GenericSpecification<>(criteria);
        when(path.getJavaType()).thenReturn((Class) BigDecimal.class);

        // when
        spec.toPredicate(root, query, cb);

        // then
        verify(cb).greaterThan(any(), eq(new BigDecimal("150.50")));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldCreateLikePredicateWithWildcardsAndLowerCase() {
        // given
        var criteria = new SearchCriteria("email", Operation.LIKE, "Test");
        var spec = new GenericSpecification<>(criteria);
        when(path.getJavaType()).thenReturn((Class) String.class);
        
        Expression<String> stringExpr = mock(Expression.class);
        when(path.as(String.class)).thenReturn(stringExpr);
        when(cb.lower(any())).thenReturn(stringExpr);

        // when
        spec.toPredicate(root, query, cb);

        // then
        verify(cb).like(eq(stringExpr), eq("%test%"));
    }
}
