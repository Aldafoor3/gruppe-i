import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import {FormsModule} from "@angular/forms";
import { ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginWindowComponent } from './views/login-window/login-window.component';
import { RegisterWindowComponent } from './views/register-window/register-window.component';
import { ToolbarComponent } from './components/toolbar/toolbar.component';
import { AppRoutingModule } from './app-routing.module';
import { ProfilWindowComponent } from './views/profil-window/profil-window.component';
import { TestWindowComponent } from './testing/views/test-window/test-window.component';
import { DemoDataSetComponent } from './testing/views/demoDB/demo-data-set/demo-data-set.component';

import { DemoDataSetService} from "./testing/views/demoDB/demo-data-set/demo-data-set.service";
import { SingleDataViewComponent} from "./testing/views/demoDB/demo-data-set/single-data-view/single-data-view.component";
import {NgOptimizedImage} from "@angular/common";
import { NgJsonEditorModule} from "ang-jsoneditor";
import { GeoDataMapComponent } from './views/geodata-map/geo-data-map.component';



import {TicketWindowComponent} from "./views/ticket-window/ticket-window/ticket-window.component";
import {TicketlistComponent} from "./views/ticket-window/ticketlist-window/ticketlist.component";
import { ChatdemoComponent } from './views/chatdemo/chatdemo.component';
import {ChatserviceService} from "./services/chatservice.service";
import { FriendListComponent } from './views/friend-list/friend-list.component';
import {ChatbotComponent} from "./views/chatbot-window/chatbot.component";
import { ForumViewComponent } from './views/forum-view/forum-view.component';
import { DiscussionViewComponent } from './views/forum-view/discussion-view/discussion-view.component';
import {ColorPickerModule} from "ngx-color-picker";

@NgModule({
  declarations: [
    AppComponent,
    LoginWindowComponent,
    RegisterWindowComponent,
    ToolbarComponent,
    ProfilWindowComponent,
    TestWindowComponent,
    DemoDataSetComponent,
    SingleDataViewComponent,
    GeoDataMapComponent,
    TestWindowComponent,
    TicketWindowComponent,
    TicketlistComponent,
    ChatdemoComponent,
    FriendListComponent,
   ChatbotComponent,
   ForumViewComponent,
   DiscussionViewComponent

  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        ReactiveFormsModule,
        NgOptimizedImage,
        NgJsonEditorModule,
        ColorPickerModule
    ],
  providers: [DemoDataSetService, ChatserviceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
