import React from 'react';

import Header from './common/Header';
import Footer from './common/Footer';

import Introduction from './main/Introduction';
import ManualPreviewGeneration from './main/ManualPreviewGeneration';

class App extends React.Component {

  /**
   * Return HTML snippet
   */
  render() {

        // return main page
        return (
          <div>
            <Header></Header>
            
            <Introduction/>
            <ManualPreviewGeneration />

            <Footer></Footer>
          </div>
        );

  }

}

export default App;