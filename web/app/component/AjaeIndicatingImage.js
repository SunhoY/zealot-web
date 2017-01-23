import React, {Component} from 'react';

const styles = {
    container: {
        width: "100%",
        height: "800px",

    }
};

class AjaeIndicatingImage extends Component {
    constructor(props) {
        super(props);
        this.percentage = props.percentage;
        if (this.percentage > 0 && this.percentage <= 70) {
            this.ajaeClass = "not_ajae_image";
        } else if (this.percentage > 70 && this.percentage < 90) {
            this.ajaeClass = "medium_ajae_image";
        } else {
            this.ajaeClass = "full_ajae_image";
        }
    }

    render() {
        return (
            <div style={styles.container} className={this.ajaeClass}>
            </div>
        )
    }
}

export default AjaeIndicatingImage;