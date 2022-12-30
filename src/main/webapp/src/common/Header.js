import React from 'react';


class Header extends React.Component {

    /**
     * Return HTML snippet
     */
    render() {

        // final output
        return (
            <div className='fixed-top'>
                <nav className="navbar bg-light">
                    <div className="container-fluid">
                        <a className="navbar-brand" href="/">Preview Service</a>
                        <button className="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="offcanvas offcanvas-end" tabIndex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
                            <div className="offcanvas-header">
                                <h5 className="offcanvas-title" id="offcanvasNavbarLabel">Preview Service</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                            </div>
                            <div className="offcanvas-body">
                                <ul className="navbar-nav justify-content-end flex-grow-1 pe-3">
                                    <li className="nav-item">
                                        <a className="nav-link active" aria-current="page" href="https://github.com/meixxi/preview-service">Project Sources <small><i>(github.com)</i></small></a>
                                    </li>
                                    <li className="nav-item">
                                        <a className="nav-link active" aria-current="page" href="https://www.linkedin.com/in/meiXXI">About the Author <small><i>(linkedin.com)</i></small></a>
                                    </li>
                                    <li className="nav-item">
                                        &nbsp;
                                    </li>
                                    <li className="nav-item">
                                    <a className="nav-link active" aria-current="page" href="https://www.tudublin.ie">TU Dublin</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </nav>
                <div className='text-muted ms-3 mt-1'>
                    <small>
                        Author: Stefan Meissner (<a href="https://www.linkedin.com/in/meiXXI"><i>meiXXI</i></a>) <br />
                        PhD Candidate at TU Dublin
                    </small>
                </div>
            </div>
        );
    }
}

export default Header;