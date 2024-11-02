import {Component, OnInit} from '@angular/core';
import { ChatbotServiceService } from "./chatbotservice.service";
import {Util} from "leaflet";




@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements OnInit{

  messages: Message[] = [];
  userInput: string = '';



  constructor(private chatbotService: ChatbotServiceService) {}

  ngOnInit() {
    this.messages.push({
      content: 'Wie kann ich dir behilflich sein?',
      from: 'bot'
    });
  }
  isGifUrl1(content: string): boolean {
    return content.endsWith('.gif');
  }

  sendMessage() {
    this.messages.push({
      content: this.userInput,
      from: 'user'
    });
    this.userInput = '';

    const lastMessage = this.messages[this.messages.length - 1].content.toLowerCase();

    //1
    switch (true) {  // 1
      case lastMessage.includes('nutzer') || lastMessage.includes('user'):
        this.chatbotService.countUsers().subscribe(
          userCount => {
            this.messages.push({
              content: `Es gibt momentan ${userCount} User im System.`,
              from: 'bot'
            });
          },
          error => {
            console.error('Error:', error);

          }
        );
        break
      //2
      case lastMessage.includes('admins') || lastMessage.includes('admin'):
        this.chatbotService.countAdmins().subscribe(
          adminCount => {
            this.messages.push({
              content: `Es gibt momentan ${adminCount} Admins im System.`,
              from: 'bot'
            });
          },
          error => {
            console.error('Error:', error);

          }
        );
        break;
        //3
      case lastMessage.includes('tickets') &&  lastMessage.includes('wie viele')
      || lastMessage.includes('supporttickets') && lastMessage.includes('wie viele')
      || lastMessage.includes('tickets'):
        this.chatbotService.countTickets().subscribe(
          ticketCount => {
            this.messages.push({
              content: `Es gibt momentan ${ticketCount} Tickets im System.`,
              from: 'bot'
            });
          },
          error => {
            console.error('Error:', error);

          }
        );
        break;
        //4
      case lastMessage.includes('freunde') && lastMessage.includes('hinzufügen') || lastMessage.includes("freunde") && lastMessage.includes('adden'):
        this.messages.push({
          content: 'Freunde fügst du hinzu, indem du die Friendlist in deinem Profil öffnest und dem jeweiligen User eine Anfrage schickst.',
          from: 'bot'
        });

        this.messages.push({
          content: 'assets/friendlistgif.gif',
          from: 'bot',
          isGif: true
        });
        break;
        //4
      case lastMessage.includes('funktionen') || lastMessage.includes('!funktionen'):
        this.messages.push({
          content: 'Es gibt einige Funktionen, die du nutzen kannst.<br>' +
            '• Forum<br>' +
            '• Chat<br>' +
            '• Anzeige von Geodaten<br>' +
            '• Datensätze<br>' +
            '• Freundesliste<br>' +
            '<br>' +
            '→ Stelle mir gerne Fragen zu den einzelnen Funktionen 😇',
          from: 'bot'
        });
        break;
        //5
      case lastMessage.includes('forum') :
        const forumLink: string = 'http://localhost:4200/forum';
        this.messages.push({
          content: `Zum Diskussionsforum gelangst du, indem du auf den "Forum"-Button in der Toolbar klickst.
            Du kannst dort eigene Beiträge eröffnen und bei anderen Usern kommentieren. <a href="${forumLink}">Link</a><br>
            Weitere Erklärungen zum Forum erhälst du hier:<br>
              !diskussion<br>
              !kommentar<br>
              !favorisieren<br>
              !melden`,
          from: 'bot'
        });
        break;
        //6
      case lastMessage.includes('diskussion') && lastMessage.includes('eröffnen')
            || lastMessage.includes('message') || lastMessage.includes('!diskussion'):
        this.messages.push({
          content: 'Diskussionen über Datensätze eröffnest du, indem du in der Toolbar auf "Diskussionsforum" klickst.' +
            ' Füge deinem Beitrag eine Überschrift und eine Kategorie hinzu.',
          from: 'bot'
        });
        break;
        //7
      case lastMessage.includes('kommentar') || lastMessage.includes('kommentieren') || lastMessage.includes('!kommentar'):
        this.messages.push({
          content: 'Kommentiere Beiträge anderer Nutzer, indem du auf den Kommentar-Button neben dem Diskussionsbeitrag klickst.',
          from: 'bot'
        });
        break;
        //8
      case lastMessage.includes('favorisieren') || lastMessage.includes('Favoriten') || lastMessage.includes('!favorisieren') :
        this.messages.push({
          content: 'Wenn du über einen Beitrag weiterhin informiert werden möchtest, kannst du ihn favorisieren. ' +
            'und Updates per Email bekommen.',
          from: 'bot'
        });
        break;
        //9
      case lastMessage.includes('melden') || lastMessage.includes('!melden'):
        const ticketLink1: string = 'http://localhost:4200/ticket';
        this.messages.push({
          content: `Sollte ein Beitrag im Forum dir unangemessen erscheinen, wende dich bitte an einen Admin: <a href="${ticketLink1}">Link</a>`,
          from: 'bot'
        });
        break;
        //10
      case lastMessage.includes('chat') || lastMessage.includes('chatten'):
        const chatLink: string = 'http://localhost:4200/chatDemo'
        this.messages.push({
          content: `Über den Chat kannst du mit anderen Usern chatten. Du erreicht den Chat über den "Chat"
                    Button in der Toolbar oder über diesen Link: <a href="${chatLink}">Link</a>`,
          from: 'bot'
        });
        break;
        //11
      case lastMessage.includes('geodaten'):
        const geodataLink: string = 'http://localhost:4200/geoData';
        this.messages.push({
          content: `Geodaten lässt du dir anzeigen, indem du auf "Geodaten" in der Toolbar klickst
                    dort kannst du eigene Geodaten hochladen und anzeigen lassen 🌍🧳. <a href="${geodataLink}">Link</a>`,
          from: 'bot'
        });
        break;
        //12
      case lastMessage.includes('supportticket') && lastMessage.includes('bearbeitet'):
        const ticketLink2: string = 'http://localhost:4200/ticket';
        this.messages.push({
          content: `Wenn dein Ticket bearbeitet wurde, erhälst du eine E-Mail. Klicke auf den Link, um ein weiteres zu senden,
                    falls dein Ticket nach 7 Tagen immer noch unbearbeitet ist: <a href="${ticketLink2}">Link</a>`,
          from: 'bot'
        });
        break;
        //13
      case lastMessage.includes('supportticket'):
        const ticketLink: string = 'http://localhost:4200/ticket';
        this.messages.push({
          content: `Um ein Supportticket zu senden, klickst du auf den Supportticket-Button.
                    Alternativ kannst du auch diesen Link hier öffnen: <a href="${ticketLink}">Link</a>`,
          from: 'bot'
        });
        break;
        //14
      case lastMessage.includes('datensätze') && lastMessage.includes('formaten')
      || lastMessage.includes('datensätze') && lastMessage.includes('formate') :
        this.messages.push({
          content: 'Es gibt Dateien im XML- und im CSV-Format.',
          from: 'bot'
        });
        break;
        //15
      case lastMessage.includes('datensätze') && lastMessage.includes('anzeigen') ||
      lastMessage.includes('Diagrammtypen'):
        this.messages.push({
          content: 'Die Datensätze kannst du als Treemap und als Säulendiagramm anzeigen lassen.' +
            ' In deinem Profil kannst du sie als Säulendiagramm und Kuchendiagramm speichern.',
          from: 'bot'
        });
        break;
      //16
      case lastMessage.includes('datensätze') && lastMessage.includes('hochladen') || lastMessage.includes('!upload'):
        this.messages.push({
          content: 'Datensätze kannst du hochladen, indem du auf "Upload Dataset" klickst und anschließend auf "Update".',
          from: 'bot'
        });

        this.messages.push({
          content: 'assets/data.gif',
          from: 'bot',
          isGif: true
        });
        break;
        //17
      case lastMessage.includes('datensätze') || lastMessage.includes('datasets') || lastMessage.includes('dataset'):
        const dataSetLink: string = 'http://localhost:4200/testData';
        this.messages.push({
          content: `Um zu den Datensätzen zu gelangen, klicke in der Toolbar auf "Datensätze": <a href="${dataSetLink}">Link</a>`,
          from: 'bot'
        });
        break;
        //18
      case lastMessage.includes('datensätze') && lastMessage.includes('runterladen') || lastMessage.includes('pdf'):
        this.messages.push({
          content: 'Alle Datensätze können mit den passenden Diagrammen heruntergeladen werden.' +
            'Für eine ausführlichere Erklärung gib !upload ein',
          from: 'bot'
        });
        break;
        //19
      case lastMessage.includes('profil') && lastMessage.includes('löschen'):
        this.messages.push({
          content: 'Ein selbständiges Löschen deines Profils ist leider nicht möglich.' +
            'Du hast allerdings die Möglichkeit, über ein Supportticket einen Admin darüber zu informieren',
          from: 'bot'
        });
        break;
        //20
      case lastMessage.includes('profil') && lastMessage.includes('privat') || lastMessage.includes('privatsphäre') ||
      lastMessage.includes('privatsphäreeinstellung'):
        this.messages.push({
          content: 'Du kannst die Privatsphäreeinstellungen deines Profil über den Regler oben links anpassen.',
          from: 'bot'
        });
        this.messages.push({
          content: 'assets/profilgif.gif',
          from: 'bot',
          isGif: true
        });
        break;


        //cheatsheet
      case lastMessage.includes('cheatsheet'):
        this.messages.push({
          content: 'Botbefehle:<br>' +
            '- user<br>' +
            '- admin<br>' +
            '- tickets<br>' +
            '- funktionen<br>' +
            '- forum<br>' +
            '- diskussion eröffnen<br>' +
            '- kommentar<br>' +
            '- melden<br>' +
            '- chat<br>' +
            '- geodaten<br>'+
            '- supportticket bearbeitet<br>'+
            '- supportticket<br>'+
            '- datensätze formate<br>' +
            '- datensätze hochladen<br>' +
            '- profil privat<br>' +
            '- cat',
          from: 'bot'
        });
        break;
        //cat-GIF
      case lastMessage.includes('katzen') || lastMessage.includes('katzenbild') || lastMessage.includes('cat') || lastMessage.includes('cats'):
        this.messages.push({
          content: 'assets/cuteCat.gif',
          from: 'bot',
          isGif: true
        });
        break;
      //meme
      case lastMessage.includes('crack'):
        this.messages.push({
          content: 'assets/crackKid.gif',
          from: 'bot',
          isGif: true
        });
        break;

        //default
      default:
        this.messages.push({
          content: 'Entschuldigung, ich kann diese Frage leider nicht beantworten. Nutze !funktionen um das System besser kennenzulernen.',
          from: 'bot'
        });
        break;
    }
  }
}

interface Message {
  content: string;
  from: 'bot' | 'user';
  isGif?: boolean;

}
