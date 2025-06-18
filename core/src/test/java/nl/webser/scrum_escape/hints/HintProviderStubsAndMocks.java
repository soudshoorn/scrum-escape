package nl.webser.scrum_escape.hints;

/**
 * Demonstratie van stubs en mocks voor HintProvider.
 * Bevat vier klassen en voorbeeldcode voor gebruik.
 */
public class HintProviderStubsAndMocks {

    // --- Stub 1 ---
    public static class AlwaysSameHintStub implements HintProvider {
        @Override
        public String getHint() {
            return "Altijd dezelfde hint";
        }
    }

    // --- Stub 2 ---
    public static class EmptyHintStub implements HintProvider {
        @Override
        public String getHint() {
            return "";
        }
    }

    // --- Mock 1 ---
    public static class HintProviderMock implements HintProvider {
        public boolean wasCalled = false;

        @Override
        public String getHint() {
            wasCalled = true;
            return "Mock hint";
        }
    }

    // --- Mock 2 ---
    public static class CountingHintProviderMock implements HintProvider {
        public int callCount = 0;

        @Override
        public String getHint() {
            callCount++;
            return "Mock hint";
        }
    }

    // --- Voorbeeldcode voor gebruik ---
    public static void main(String[] args) {
        // Stub 1
        HintProvider stub1 = new AlwaysSameHintStub();
        System.out.println("Stub 1: " + stub1.getHint());

        // Stub 2
        HintProvider stub2 = new EmptyHintStub();
        System.out.println("Stub 2: '" + stub2.getHint() + "'");

        // Mock 1
        HintProviderMock mock1 = new HintProviderMock();
        mock1.getHint();
        System.out.println("Mock 1 wasCalled: " + mock1.wasCalled);

        // Mock 2
        CountingHintProviderMock mock2 = new CountingHintProviderMock();
        mock2.getHint();
        mock2.getHint();
        System.out.println("Mock 2 callCount: " + mock2.callCount);
    }
} 