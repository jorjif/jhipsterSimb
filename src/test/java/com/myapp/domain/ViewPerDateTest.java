package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewPerDateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewPerDate.class);
        ViewPerDate viewPerDate1 = new ViewPerDate();
        viewPerDate1.setId(1L);
        ViewPerDate viewPerDate2 = new ViewPerDate();
        viewPerDate2.setId(viewPerDate1.getId());
        assertThat(viewPerDate1).isEqualTo(viewPerDate2);
        viewPerDate2.setId(2L);
        assertThat(viewPerDate1).isNotEqualTo(viewPerDate2);
        viewPerDate1.setId(null);
        assertThat(viewPerDate1).isNotEqualTo(viewPerDate2);
    }
}
