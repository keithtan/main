package seedu.address.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.student.exceptions.DuplicatePersonException;
import seedu.address.model.student.exceptions.PersonNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A student is considered unique by comparing using {@code Student#isSamePerson(Student)}. As such, adding and updating
 * of persons uses Student#isSamePerson(Student) for equality so as to ensure that the student being added or updated is
 * unique in terms of identity in the UniqueStudentList. However, the removal of a student uses Student#equals(Object)
 * so as to ensure that the student with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Student#isSamePerson(Student)
 */
public class UniqueStudentList implements Iterable<Student> {

    private final ObservableList<Student> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent student as the given argument.
     */
    public boolean contains(Student toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSamePerson);
    }

    /**
     * Adds a student to the list.
     * The student must not already exist in the list.
     */
    public void add(Student toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the list.
     * The student identity of {@code editedStudent} must not be the same as another existing student in the list.
     */
    public void setPerson(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.isSamePerson(editedStudent) && contains(editedStudent)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedStudent);
    }

    /**
     * Removes the equivalent student from the list.
     * The student must exist in the list.
     */
    public void remove(Student toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new PersonNotFoundException();
        }
    }

    public void setPersons(UniqueStudentList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setPersons(List<Student> students) {
        requireAllNonNull(students);
        if (!personsAreUnique(students)) {
            throw new DuplicatePersonException();
        }

        internalList.setAll(students);
    }

    public Optional<Student> getStudentWithId(StudentId id) {
        return internalList.stream().filter(st -> st.getStudentId().equals(id))
            .findFirst();
    }

    /**
     * Returns a clone of this list.
     */
    public UniqueStudentList clone() {
        UniqueStudentList other = new UniqueStudentList();
        other.internalList.addAll(this.internalList);
        return other;
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Student> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Student> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueStudentList // instanceof handles nulls
                        && internalList.equals(((UniqueStudentList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code students} contains only unique students.
     */
    private boolean personsAreUnique(List<Student> students) {
        for (int i = 0; i < students.size() - 1; i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (students.get(i).isSamePerson(students.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}