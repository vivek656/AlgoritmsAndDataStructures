package algorithms;

import common.AbstractAlgoAndDsTestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class KarpRabinStringSearchTests extends AbstractAlgoAndDsTestBase {

    @Test
    void karpRabinTest() throws IOException {
        var inputString = "hello how are you , with advent of chatGPT every new thing feels like sunday, i dont know what the hell" +
                " i am writing here but that s write , putting chatGPT here as well just to check chatGPT";
        var searchString = "chatGPT";
        var cell = KarpRabinStringSearch.search(searchString, inputString);
        logger.info(LOG_INFO, cell.offsets);
    }

}
