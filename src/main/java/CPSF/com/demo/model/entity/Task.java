package CPSF.com.demo.model.entity;

import CPSF.com.demo.model.constant.TaskStatus;
import CPSF.com.demo.model.constant.TaskType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.Nullable;

@Entity
@Getter
@Setter
@Table(name = "system_task")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Task extends DbObject {

    @Column(name = "target_id")
    @NotNull
    private String targetId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "json")
    @Nullable
    private Object payload;
    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus taskStatus;
    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskType taskType;
    @Column(name = "retry_count")
    @NotNull
    private long retryCount;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "parent_task_id")
    @Nullable
    private Task parentTask;
}
