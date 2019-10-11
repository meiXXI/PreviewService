import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewGeneratorComponent } from './preview-generator.component';

describe('PreviewGeneratorComponent', () => {
  let component: PreviewGeneratorComponent;
  let fixture: ComponentFixture<PreviewGeneratorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PreviewGeneratorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviewGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
