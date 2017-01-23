import React, {Component} from 'react';
import classNames from 'classnames';

const styles = {
    container: {
        textAlign: 'center',
        marginTop: "100px"
    }
};

class AjaeIndicatingText extends Component {
    constructor(props) {
        super(props);

        let percentage = props.percentage;
        let ajaeMessageClass,
            ajaeMessageColor;

        console.log(percentage);

        if(percentage >= 0 && percentage < 70) {
            ajaeMessageClass = "not_ajae_message";
            ajaeMessageColor = "not_ajae_color";
            this.ajaeMessage = "자라나는 어린새짝";
        } else if(percentage >= 70 && percentage < 90) {
            ajaeMessageClass = "medium_ajae_message";
            ajaeMessageColor = "medium_ajae_color";
            this.ajaeMessage = "아직까진 젊은오빠";
        } else {
            ajaeMessageClass = "full_ajae_message";
            ajaeMessageColor = "full_ajae_color";
            this.ajaeMessage = "빼박캔트 진성아재";
        }

        this.ajaeClasses = classNames(ajaeMessageClass, ajaeMessageColor);
    }

    render() {
        return(<div style={styles.container} className={this.ajaeClasses}>{this.ajaeMessage}</div>);
    }
}

export default AjaeIndicatingText;