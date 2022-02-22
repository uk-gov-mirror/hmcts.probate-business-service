package uk.gov.hmcts.probate.services.businessdocuments.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.probate.model.documents.CheckListItemType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckListItem {
    private CheckListItemType type;
    private String text;
    private String url;
    private String beforeLinkText;
    private String afterLinkText;
}
