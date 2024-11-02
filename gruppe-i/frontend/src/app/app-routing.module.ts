import { NgModule } from '@angular/core';
import {RouterModule,Routes} from '@angular/router';

import {LoginWindowComponent} from "./views/login-window/login-window.component";
import {RegisterWindowComponent} from "./views/register-window/register-window.component";
import {ProfilWindowComponent} from "./views/profil-window/profil-window.component";
import {DemoDataSetComponent} from "./testing/views/demoDB/demo-data-set/demo-data-set.component";
import {SingleDataViewComponent} from "./testing/views/demoDB/demo-data-set/single-data-view/single-data-view.component"
import {TicketWindowComponent} from "./views/ticket-window/ticket-window/ticket-window.component";
import {TicketlistComponent} from "./views/ticket-window/ticketlist-window/ticketlist.component";
import {ChatdemoComponent} from "./views/chatdemo/chatdemo.component";
import {FriendListComponent} from "./views/friend-list/friend-list.component";
import {ChatbotComponent} from "./views/chatbot-window/chatbot.component";

//testing
import { TestWindowComponent } from './testing/views/test-window/test-window.component';
import {GeoDataMapComponent} from "./views/geodata-map/geo-data-map.component";
import {ForumViewComponent} from "./views/forum-view/forum-view.component";
import {DiscussionViewComponent} from "./views/forum-view/discussion-view/discussion-view.component";


const routes: Routes = [
  {path: '', redirectTo:'login', pathMatch:'full'},
  {path: 'login', component: LoginWindowComponent},
  {path: 'registration', component: RegisterWindowComponent},
  {path: 'profil/:id', component: ProfilWindowComponent},
  {path: 'testWindow', component: TestWindowComponent},
  {path: 'testData', component: DemoDataSetComponent},
  {path: 'testData/:id', component: SingleDataViewComponent},
  {path: 'geoData', component: GeoDataMapComponent},
  {path: 'ticket', component: TicketWindowComponent},
  {path: 'ticketlist', component: TicketlistComponent},
  {path: 'chatDemo', component: ChatdemoComponent},
  {path: 'friendListDemo', component: FriendListComponent},
  {path: 'chatbot', component: ChatbotComponent},
  {path: 'forum', component: ForumViewComponent},
  {path: 'forum/:id', component: DiscussionViewComponent},
  { path: '**', redirectTo:'geoData'},  // Wildcard route for a 404 page
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports:[RouterModule]
})
export class AppRoutingModule { }
