import React from 'react';

import Header from './common/Header';
import Footer from './common/Footer';

import Introduction from './main/Introduction';
import ManualPreviewGeneration from './main/ManualPreviewGeneration';
import AutomatedPreviewGenerationXJdf from './main/AutomatedPreviewGenerationXJdf';
import AutomatedPreviewGenerationProprietary from './main/AutomatedPreviewGenerationProprietary';
import ServerLogging from './common/ServerLogging';

class App extends React.Component {

  /**
   * Return HTML snippet
   */
  render() {

    // return main page
    return (
      <div className='h-100 d-flex flex-column'>
        <div>
          <Header></Header>
        </div>

        <div className='flex-fill overflow-auto'>
          <Introduction />
          <ManualPreviewGeneration />
          <AutomatedPreviewGenerationXJdf />
          <AutomatedPreviewGenerationProprietary />
        </div>

        <div>
          <ServerLogging />
        </div>
        <div>
          <Footer></Footer>
        </div>
      </div>
    );

  }

}

export default App;