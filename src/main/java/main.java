


public class main {
    public static void main(String[] args) {
       // MessengerView hej = new MessengerView();
       // hej.loadTemplatesMap("data.properties");

                MessagesHandlerModel msgsModel = new MessagesHandlerModel();
                TemplatesHandlerModel msgrModel = new TemplatesHandlerModel();
                PCMHandlerModel pcmHandler = new PCMHandlerModel();
                PCMFetcherModel pcmFetcher = new PCMFetcherModel();
                PCMSenderModel pcmSender = new PCMSenderModel();

                Controller controller = new Controller();

                MessengerView msgrView = new MessengerView(controller);
                MessagesView msgsView = new MessagesView();

                controller.addViews(msgrView, msgsView);

                controller.addModels(msgrModel, msgsModel, pcmHandler, pcmFetcher, pcmSender);

                controller.initialize();
    }
}