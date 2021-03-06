/* 
 * Copyright 2016 Yannick Roffin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { Component, Input, OnInit } from '@angular/core';

import { Message } from 'primeng/primeng';

import { JarvisDataDeviceService } from '../../service/jarvis-data-device.service';
import { JarvisDataViewService } from '../../service/jarvis-data-view.service';
import { JarvisDataStoreService } from '../../service/jarvis-data-store.service';

/**
 * data model
 */
import { DeviceBean } from '../../model/device-bean';
import { ViewBean } from '../../model/view-bean';
import { Oauth2Bean, MeBean } from '../../model/security/oauth2-bean';

@Component({
  selector: 'app-jarvis-desktop',
  templateUrl: './jarvis-desktop.component.html',
  styleUrls: ['./jarvis-desktop.component.css']
})
export class JarvisDesktopComponent implements OnInit {

  msgs: Message[] = [];
  myViews: ViewBean[];

  constructor(
    private _jarvisDataDeviceService: JarvisDataDeviceService,
    private _jarvisDataStoreService: JarvisDataStoreService,
    private _jarvisDataViewService: JarvisDataViewService) {
  }

  ngOnInit() {
    /**
     * get profile from store
     */
    this.myViews = this._jarvisDataStoreService.getViews()
  }

  private touch(device: DeviceBean): void {
    this._jarvisDataDeviceService.Task(device.id, "execute", {})
      .subscribe(
      (data: any) => this.msgs.push({severity:'info', summary:'Activation', detail:device.name})
      );
  }
}
