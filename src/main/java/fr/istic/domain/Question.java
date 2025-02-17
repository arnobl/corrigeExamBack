package fr.istic.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.json.bind.annotation.JsonbTransient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import fr.istic.domain.enumeration.GradeType;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cacheable
@RegisterForReflection
public class Question extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Column(name = "numero", nullable = false)
    public Integer numero;

    @Column(name = "point")
    public Integer quarterpoint;

    @Column(name = "step")
    public Integer step;

    @Column(name = "valid_expression")
    public String validExpression;

    @Column(name = "libelle")
    public String libelle;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade_type")
    public GradeType gradeType;

    @Column(name = "defaultpoint")
    public Integer defaultpoint;

    @Column(name = "randomhorizontalcorrection")
    public Boolean randomHorizontalCorrection;

    @Column(name = "canexceedthemax")
    public Boolean canExceedTheMax;

    @Column(name = "canbenegative")
    public Boolean canBeNegative;

    @Column(name = "mustbeignoreinglobalscale")
    public Boolean mustBeIgnoreInGlobalScale;



    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    public Zone zone;

    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<TextComment> textcomments = new HashSet<>();

    @OneToMany(mappedBy = "question",cascade = CascadeType.REMOVE)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<GradedComment> gradedcomments = new HashSet<>();

    @OneToMany(mappedBy = "question")
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public Set<HybridGradedComment> hybridcomments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonbTransient
    public QuestionType type;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonbTransient
    public Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", numero=" + numero +
            ", point=" + quarterpoint +
            ", step=" + step +
            ", validExpression='" + validExpression + "'" +
            ", libelle='" + libelle + "'" +
            ", gradeType='" + gradeType + "'" +
            "}";
    }

    public Question update() {
        return update(this);
    }

    public Question persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Question update(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("question can't be null");
        }
        var entity = Question.<Question>findById(question.id);
        if (entity != null) {
            entity.numero = question.numero;
            entity.quarterpoint = question.quarterpoint;
            entity.step = question.step;
            entity.validExpression = question.validExpression;
            entity.libelle = question.libelle;
            entity.gradeType = question.gradeType;
            entity.defaultpoint = question.defaultpoint;
            entity.zone = question.zone;
            entity.textcomments = question.textcomments;
            entity.gradedcomments = question.gradedcomments;
            entity.hybridcomments = question.hybridcomments;
            entity.type = question.type;
            entity.exam = question.exam;
            entity.randomHorizontalCorrection = question.randomHorizontalCorrection;
            entity.canBeNegative = question.canBeNegative;
            entity.canExceedTheMax = question.canExceedTheMax;
            entity.mustBeIgnoreInGlobalScale  =question.mustBeIgnoreInGlobalScale;
        }
        return entity;
    }

    public static Question persistOrUpdate(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("question can't be null");
        }
        if (question.id == null) {
            persist(question);
            return question;
        } else {
            return update(question);
        }
    }


    public static long deleteAllExamId( long examId) {
        return delete("delete from Question question where question.exam.id =?1", examId);
    }

    public static PanacheQuery<Question> findQuestionbyExamId( long examId) {
        return find("select question from Question question where question.exam.id =?1", examId);
    }
    public static PanacheQuery<Question> findQuestionbyExamIdandnumero( long examId, int numero) {
        return find("select question from Question question where question.exam.id =?1 and question.numero=?2", examId, numero);
    }
    public static PanacheQuery<Question> findQuestionbyZoneId( long zoneId) {
        return find("select question from Question question where question.zone.id =?1", zoneId);
    }

    public static PanacheQuery<Question> canAccess(long qId, String login) {
        return find("select q from Question q join q.exam.course.profs as u where q.id =?1 and u.login =?2", qId, login);
    }


}
