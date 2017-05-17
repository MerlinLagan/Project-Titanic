// Vi vill kunna få timestamps / estimates vid olika polygoner och skapa en översikt av detta (info från flera aktörer)
// Vi vill kunna vara redo innan ett fartyg ska kontakta oss
// Vi vill kunna hitta de states som ex "entry_request" och "request_tO_switch_berth" och få upp detta
// Vi vill kunna formatera Portcallmessages så att de funkar med input från frontenden
// Vi vill kunna visa viktig info från olika meddelanden i frontenden


public class Main {
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