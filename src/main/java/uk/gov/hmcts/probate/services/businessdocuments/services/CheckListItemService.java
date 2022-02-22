package uk.gov.hmcts.probate.services.businessdocuments.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.probate.model.documents.BulkScanCoverSheet;
import uk.gov.hmcts.reform.probate.model.documents.BusinessDocument;

@Slf4j
@Component
public class CheckListItemService {
//    public BusinessDocument buildCheckListItems(BusinessDocument businessDocument) throws JsonProcessingException {
//        log.info(String.valueOf(businessDocument));
//        if (businessDocument instanceof BulkScanCoverSheet) {
//            BulkScanCoverSheet bulkScanCoverSheet = (BulkScanCoverSheet) businessDocument;
//            log.info("!!!!!");
//            log.info(bulkScanCoverSheet.getCheckListItemsString());
//            ObjectMapper mapper = new ObjectMapper();
//            CheckListItem checkListItems = mapper.readValue(bulkScanCoverSheet.getCheckListItemsString(), CheckListItem.class);
//
//            log.info(String.valueOf(checkListItems)); //John
//            // map check list items to check list item java object
//
//            return bulkScanCoverSheet;
//        }
//        return businessDocument;
//    }
}
