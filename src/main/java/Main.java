// VesselLocations.java ≤≤≤≤ Vi vill kunna få timestamps / estimates vid olika polygoner och skapa en översikt av detta (info från flera aktörer)
// Backenden ska kunna sålla ut detta utefter vad andra skickat ≤≤≤≤ Vi vill kunna vara redo innan ett fartyg ska kontakta oss
// Backenden ska kunna sålla ut detta utefter vad andra skickat ≤≤≤≤ Vi vill kunna hitta de states som ex "entry_request" och "request_tO_switch_berth" och få upp detta
// Vi vill kunna formatera PortCallMessages så att de funkar med input från frontenden, med så lite input som möjligt från användaren
// Vi vill kunna visa viktig info från olika meddelanden i frontenden


// Se till att PCMHandlerModel har koll på vilket meddelande som visas just nu, samt meddelanden som ej ännu visats
// tillsammans med previous / nextmessage (viktigt med ordningen)
// se till att man inte kan gå längre än vad det finns meddelanden

public class Main {
    public static void main(String[] args) {

                MessagesHandlerModel msgsModel = new MessagesHandlerModel();
                TemplatesHandlerModel msgrModel = new TemplatesHandlerModel();
                PCMHandlerModel pcmHandler = new PCMHandlerModel();
                PCMFetcherModel pcmFetcher = new PCMFetcherModel();
                PCMSenderModel pcmSender = new PCMSenderModel();
                VesselLocationModel vsllocModel = new VesselLocationModel();

                Controller controller = new Controller();

                MessengerView msgrView = new MessengerView(controller);
                MessagesView msgsView = new MessagesView(controller);
                VesselLocationView vsllocView = new VesselLocationView(controller);

                controller.addViews(msgrView, msgsView, vsllocView);

                controller.addModels(msgrModel, msgsModel, pcmHandler, pcmFetcher, pcmSender, vsllocModel);

                controller.initialize();
    }
}