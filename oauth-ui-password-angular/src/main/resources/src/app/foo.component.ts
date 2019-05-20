import { Component } from '@angular/core';
import {AppService, Foo} from './app.service'

@Component({
  selector: 'foo-details',
  providers: [AppService],  
  templateUrl: './dashboard.component.html'
})

export class FooComponent {
    public foo = new Foo('1','sample foo',23,'sdfsfg');
    private foosUrl = 'http://localhost:8085/resource/dashboards';  

    constructor(private _service:AppService) {}

    getFoo(){
        this._service.getResource(this.foosUrl)
         .subscribe(
                     data => this.foo = data,
                     error =>  this.foo.name = 'Error');
    }
}
