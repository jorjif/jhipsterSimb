package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewsToDateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewsToDate.class);
        ViewsToDate viewsToDate1 = new ViewsToDate();
        viewsToDate1.setId(1L);
        ViewsToDate viewsToDate2 = new ViewsToDate();
        viewsToDate2.setId(viewsToDate1.getId());
        assertThat(viewsToDate1).isEqualTo(viewsToDate2);
        viewsToDate2.setId(2L);
        assertThat(viewsToDate1).isNotEqualTo(viewsToDate2);
        viewsToDate1.setId(null);
        assertThat(viewsToDate1).isNotEqualTo(viewsToDate2);
    }
}
