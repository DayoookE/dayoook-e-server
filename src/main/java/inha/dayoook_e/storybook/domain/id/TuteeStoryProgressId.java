package inha.dayoook_e.storybook.domain.id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TuteeStoryProgressId implements Serializable {

    private Integer tuteeId;

    private Integer storybookId;
}
